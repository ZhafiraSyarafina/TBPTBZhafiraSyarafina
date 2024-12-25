const { KPRequest, Group, GroupMember, Member } = require("../models");
const { sequelize } = require("../models");
const { Op } = require("sequelize");
const fs = require("fs");
const path = require('path');
const jwt = require("jsonwebtoken");



// Get all KP requests
const getRequests = async (req, res) => {
  try {
    const requests = await KPRequest.findAll({
      include: [
        {
          model: Group,
          include: [
            {
              model: Member,
              through: GroupMember,
            },
          ],
        },
      ],
      order: [["createdAt", "DESC"]],
    });

    return res.status(200).json({
      success: true,
      data: requests,
    });
  } catch (error) {
    console.error("Error fetching requests:", error);
    return res.status(500).json({
      success: false,
      message: "Failed to fetch KP requests",
      error: error.message,
    });
  }
};

// Get specific KP request by ID
function checkUserLoggedIn(req) {
    const authHeader = req.headers['authorization'];
    const accessToken = authHeader && authHeader.split(' ')[1];
    console.log("Access Token:", accessToken);

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



const myRequest = async (req, res) => {
  try {
    
    const { user } = checkUserLoggedIn(req);
        console.log(user);
        const userId = user.userId;


    const myrequest = await KPRequest.findOne({
      include: [
        {
          model: Group,
          include: [
            {
              model: Member,
              through: GroupMember,
              where: { id: userId }
            },
          ],
        },
      ],
    });

    console.log(myrequest.id);
    const request = await KPRequest.findOne({
      where: { id: myrequest.id },
      include: [
        {
          model: Group,
          include: [
            {
              model: Member,
              through: GroupMember,
            },
          ],
        },
      ],
    });
    if (!request) {
      return res.status(404).json({
        success: false,

        message: "KP request not found",
      });
    }


    return res.status(200).json({
      success: true,

      data: { request },
    });



  } catch (error) {
    console.error("Error fetching request:", error);

    return res.status(500).json({
      success: false,

      message: "Failed to fetch KP request",

      error: error.message,
    });
  }
};

const editMyRequest = async (req, res) => {
  try {
    const { user } = checkUserLoggedIn(req);
    const userId = user.userId;
    const { startDate, endDate, company } = req.body;

    // First find the request that belongs to user's group
    const myrequest = await KPRequest.findOne({
      include: [
        {
          model: Group,
          include: [
            {
              model: Member,
              through: GroupMember,
              where: { id: userId }
            },
          ],
        },
      ],
    });

    if (!myrequest) {
      return res.status(404).json({
        success: false,
        message: "KP request not found",
      });
    }

    // Update the request
    await KPRequest.update(
      {
        startDate,
        endDate, 
        company
      },
      {
        where: { id: myrequest.id }
      }
    );

    // Fetch the updated request with all its relations
    const updatedRequest = await KPRequest.findOne({
      where: { id: myrequest.id },
      include: [
        {
          model: Group,
          include: [
            {
              model: Member,
              through: GroupMember,
            },
          ],
        },
      ],
    });

    return res.status(200).json({
      success: true,
      message: "KP request updated successfully",
      data: { request: updatedRequest },
    });

  } catch (error) {
    console.error("Error updating request:", error);
    return res.status(500).json({
      success: false,
      message: "Failed to update KP request",
      error: error.message,
    });
  }
};

// Reject KP request
const rejectRequest = async (req, res) => {
  try {
    const { id } = req.params;
    const { reason } = req.body; // Optional rejection reason

    const request = await KPRequest.findByPk(id);

    if (!request) {
      return res.status(404).json({
        success: false,
        message: "KP request not found",
      });
    }

    if (request.status !== "Pending") {
      return res.status(400).json({
        success: false,
        message: "Can only reject pending requests",
      });
    }

    await request.update({
      status: "Rejected",
      reason: reason || null,
    });

    return res.status(200).json({
      success: true,
      message: "KP request rejected successfully",
      data: request,
    });
  } catch (error) {
    console.error("Error rejecting request:", error);
    return res.status(500).json({
      success: false,
      message: "Failed to reject KP request",
      error: error.message,
    });
  }
};

// Accept KP request
const acceptRequest = async (req, res) => {
  try {
    const { id } = req.params;

    const request = await KPRequest.findByPk(id);

    if (!request) {
      return res.status(404).json({
        success: false,
        message: "KP request not found",
      });
    }

    if (request.status !== "Pending") {
      return res.status(400).json({
        success: false,
        message: "Can only accept pending requests",
      });
    }

    try {
      await request.update(
        {
          status: "Accepted",
        }
      );

      return res.status(200).json({
        success: true,
        message: "KP request accepted successfully",
        data: request,
      });
    } catch (error) {
      await t.rollback();
      throw error;
    }
  } catch (error) {
    console.error("Error accepting request:", error);
    return res.status(500).json({
      success: false,
      message: "Failed to accept KP request",
      error: error.message,
    });
  }
};

// Download KP request documents
const downloadRequest = async (req, res) => {
  try {
    const { id } = req.params;

    const request = await KPRequest.findByPk(id);

    if (!request) {
      return res.status(404).json({
        success: false,
        message: "KP request not found",
      });
    }

    if (!request.proposalUrl) {
      return res.status(404).json({
        success: false,
        message: "No proposal document found for this request",
      });
    }

    // Assuming proposalUrl contains the file path relative to your storage directory
    const filePath = path.join(
      __dirname,
      "..",
      "public",
      "proposal",
      request.proposalUrl
    );

    console.log("File path:", filePath);
    // Check if file exists
    if (!fs.existsSync(filePath)) {
      return res.status(404).json({
        success: false,
        message: "Proposal file not found on server",
      });
    }

    // Send file for download
    res.download(filePath, `KP_Proposal_${request.memberId}.pdf`, (err) => {
      if (err) {
        console.error("Error downloading file:", err);
        return res.status(500).json({
          success: false,
          message: "Error downloading file",
          error: err.message,
        });
      }
    });
  } catch (error) {
    console.error("Error processing download:", error);
    return res.status(500).json({
      success: false,
      message: "Failed to process download request",
      error: error.message,
    });
  }
};




const addRequest = async (req, res) => {
  try {
      // Destructure required fields from request body
      const { 
          groupId,
          company,
          startDate,
          endDate
      } = req.body;

      console.log(req.file);

      // Validate required fields
      if (!groupId || !company || !startDate || !endDate || !req.file) {
          return res.status(400).json({
              success: false,
              message: "All fields are required (groupId, company, startDate, endDate, proposal file)"
          });
      }

      // Get uploaded file URL
      const proposalUrl = `/uploads/proposals/${req.file.filename}`;

      // Check if group exists
      const group = await Group.findByPk(groupId);
      if (!group) {
          return res.status(404).json({
              success: false,
              message: "Group not found"
          });
      }

      // Create new KP request
      const newRequest = await KPRequest.create({
          groupId,
          company,
          startDate,
          endDate,
          proposalUrl,
          status: 'Pending' // Using the default status from model
      });

      // Fetch the created request with group details
      const request = await KPRequest.findOne({
          where: { id: newRequest.id },
          include: [{
              model: Group,
              include: [{
                  model: Member,
                  through: GroupMember
              }]
          }]
      });

      return res.status(201).json({
          success: true,
          message: "KP Request created successfully",
          data: request
      });

  } catch (error) {
      console.error("Error adding request:", error);
      return res.status(500).json({
          success: false,
          message: "Failed to create KP request",
          error: error.message
      });
  }
};



module.exports = {
  getRequests,
  myRequest,
  rejectRequest,
  acceptRequest,
  downloadRequest,
  addRequest,
  editMyRequest
};
