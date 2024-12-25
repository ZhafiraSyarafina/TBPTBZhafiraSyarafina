const jwt = require('jsonwebtoken');
const Users = require("../models/Member");
const bcrypt = require("bcryptjs");
const fs = require('fs/promises');
const multer = require('multer');
const { where } = require('sequelize');



const Login = async (req, res) => {
  try {
    const user = await Users.findOne({
      where: {
        email: req.body.email,
      },
    });

    if (!user) {
      return res.status(401).json({ message: "Email tidak ditemukan, silahkan daftar terlebih dahulu" });
    }

    console.log(user);
    const match = await bcrypt.compare(req.body.password, user.password);

    if (!match) {
      return res.status(401).json({ message: "Password salah" });
    }

    const userId = user.id;
   

    const token = jwt.sign({ userId}, process.env.ACCESS_TOKEN_SECRET, {
      expiresIn: "1d",
    });


    res.cookie("token", token, { httpOnly: true, secure: process.env.NODE_ENV === 'production' });


    res.status(200).json({
      success: true,
      message: "Login berhasil",
      data: {
        user: {
          id: userId,
          name: user.name,
          email: user.email,
          noHp: user.noHp,
          jabatan: user.institusi,
        },
        token: token,
      },
    });


    

  } catch (error) {
    console.log(error);
    res.status(401).json(error.message);
  }
};

const register = async (req, res) => {
  try {
    const { name, email, password,noHp,institusi } = req.body;

    const user = await Users.findOne({
      where: {
        email: email,
      },
    });

    if (user) {
      return res.status(400).json({ message: "Email sudah terdaftar" });
    }

    const hashedPassword = await bcrypt.hash(password, 10);

    const newUser = await Users.create({
      name: name,
      email: email,
      password: hashedPassword,
      noHp: noHp,
      jabatan: jabatan,
    });

    res.status(201).json({
      success: true,
      message: "Pengguna berhasil didaftarkan",
      data: {
        user: {
          id: newUser.id,
          email: newUser.email,
        },
      },
    });
  } catch (error) {
    console.log(error);
    res.status(500).json({ message: "Terjadi kesalahan server" });
  }
}



function checkUserLoggedIn(req) {
  const authHeader = req.headers['authorization'];
  const accessToken = authHeader && authHeader.split(' ')[1];

  console.log ("Access Token:", accessToken);
  let user = null;
  if (accessToken) {
    try {
      const decoded = jwt.verify(
        accessToken,
        process.env.ACCESS_TOKEN_SECRET
      );
      user = {
        userId: decoded.userId,
      };

      console.log("User logged in:", user);
    } catch (error) {
      console.error("Token invalid or expired:", error.message);
      return { user: null };
    }
  }
  return { user };
}

const changePassword = async (req, res) => {
  try {
    const { currentPassword, newPassword } = req.body;

    console.log("Current password:", currentPassword);
    console.log("New password:", newPassword);
    const { user } = checkUserLoggedIn(req);
    if (!user) {
      return res.status(401).json({ success: false, message: "User tidak terautentikasiiii" });
    }

    console.log("User:", user);

    const userPass = await Users.findByPk(user.userId);
    console.log("User:", userPass);


    const isPasswordValid = await bcrypt.compare(currentPassword, userPass.password);
    if (!isPasswordValid) {
      return res.status(401).json({ message: "Password saat ini salah" });
    }

    const hashedNewPassword = await bcrypt.hash(newPassword, 10);

    await userPass.update({ password: hashedNewPassword });

    return res.status(200).json({ message: "Password berhasil diubah" });
  } catch (error) {
    console.log(error);
    return res.status(500).json({ message: "Terjadi kesalahan server" });
  }
};

const editProfile = async (req, res) => {
  try {
    const { newPhoneNumber } = req.body;
  
    const { user } = checkUserLoggedIn(req);
    if (!user) {
      return res.status(401).json({ success: false, message: "User tidak terautentikasiiii" });
    }
    await user.update({ 

      phone: newPhoneNumber
    });
    return res.redirect('/profile');
  } catch (error) {
    console.log(error);
    return res.status(500).json({ message: "Terjadi kesalahan server" });
  }
};

const getUser = async (req, res) => {
  try {
    // Mengecek apakah user sudah login
    const { user } = checkUserLoggedIn(req);
    if (!user) {
      return res.status(401).json({ success: false, message: "User tidak terautentikasiiii" });
    }

    // Mendapatkan data user dari database berdasarkan userId
    const userData = await Users.findByPk(user.userId)

    if (!userData) {
      return res.status(404).json({ success: false, message: "User tidak ditemukan" });
    }

    // Mengembalikan data user sebagai JSON
    res.status(200).json({
      success: true,
      message: "Data user berhasil didapatkan",
      data: userData,
    });
  } catch (error) {
    console.error("Error fetching user:", error.message);
    res.status(500).json({ success: false, message: "Internal server error" });
  }
};

const uploadProfilePicture = async (req, res) => {
  const upload = multer({
    storage: multer.diskStorage({
      destination: async (req, file, cb) => {
        const user = await getUser(req, res);
        const userId = user.id;
        const dir = `public/data/user_${userId}`;
        await fs.mkdir(dir, { recursive: true });
        cb(null, dir);
      },
      filename: (req, file, cb) => {
        cb(null, 'profile.jpg');
      }
    })
  }).single('profile');

  await upload(req, res, async (err) => {
    if (err) {
      return res.status(400).json({ error: err.message });
    }

    await editProfile(req, res);
  });
};


module.exports = {
  Login,
  register,
  checkUserLoggedIn,
  changePassword,
  editProfile,
  getUser,
};
