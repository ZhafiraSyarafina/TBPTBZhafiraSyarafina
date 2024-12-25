const express = require("express");
const { Login,register,getUser,editProfile,changePassword } = require("../controllers/auth.js");
const {getRequests,getRequest,editMyRequest,addRequest,myRequest} = require("../controllers/request.js");
const { isUserLoggedIn } = require("../middleware/isUserLoggedIn.js");
const upload = require('../config/multer.js');
const { 
  createGroup, 
  getGroups, 
  getGroupById, 
  updateGroup, 
  deleteGroup,
  getAllMembers 
} = require('../controllers/group.js');


const router = express.Router();

router.post('/login', Login);
router.get('/login', isUserLoggedIn, (req, res) => {
  res.render('login');
});

router.get('/user', (req, res) => {
  getUser(req, res);
});

router.post('/update-phone',editProfile)
router.post('/change-password',changePassword)

router.post('/register', register);

router.get('/requests', getRequests);
router.get('/myRequest', myRequest);
router.put('/myRequest', editMyRequest);

router.post('/request', upload.single('proposal'), addRequest);

router.get('/members', getAllMembers);
router.post('/groups', createGroup);
router.get('/group', getGroups);
router.get('/groups/:id', getGroupById);
router.put('/groups/:id', updateGroup);
router.delete('/groups/:id', deleteGroup);


module.exports = router;
