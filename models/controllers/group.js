const Group = require("../models/Group");
const Member = require("../models/Member");
const GroupMember = require("../models/GroupMember");
const jwt = require("jsonwebtoken");


const getAllMembers = async (req, res) => {
    try {
      // Fetch all members from the database
      const members = await Member.findAll();
  
      // Send the retrieved members as a response
      res.status(200).json({
        success: true,
        data: members,
      });
    } catch (error) {
      // Handle errors
      console.error("Error fetching members:", error);
      res.status(500).json({
        success: false,
        message: "Failed to fetch members",
        error: error.message,
      });
    }
  };

// Create a new group and add members
const createGroup = async (req, res) => {
    try {
        const { name, memberIds } = req.body;

        // Validate required fields
        if (!name || !memberIds || !Array.isArray(memberIds) || memberIds.length === 0) {
            return res.status(400).json({
                success: false,
                message: "Group name and at least one member ID are required"
            });
        }

        // Check if all members exist
        const members = await Member.findAll({
            where: {
                id: memberIds
            }
        });

        if (members.length !== memberIds.length) {
            return res.status(404).json({
                success: false,
                message: "One or more members not found"
            });
        }

        // Create the group
        const group = await Group.create({
            name
        });

        // Add members to the group
        const groupMembers = memberIds.map(memberId => ({
            groupId: group.id,
            memberId: memberId
        }));

        await GroupMember.bulkCreate(groupMembers);

        // Fetch the created group with member details
        const createdGroup = await Group.findOne({
            where: { id: group.id },
            include: [{
                model: Member,
                through: GroupMember
            }]
        });

        return res.status(201).json({
            success: true,
            message: "Group created successfully",
            data: createdGroup
        });

    } catch (error) {
        console.error("Error creating group:", error);
        return res.status(500).json({
            success: false,
            message: "Failed to create group",
            error: error.message
        });
    }
};


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

  
// Get my groups
const getGroups = async (req, res) => {
    try {

        const { user } = checkUserLoggedIn(req);
        console.log(user);
        const userId = user.userId;

        const groupMember = await GroupMember.findOne({
            where: { memberId: userId }
        });
        console.log(groupMember);
        const groups = await Group.findOne({
            where: { id: groupMember.groupId },
            include: [{
                model: Member
            }]
        });


        

        return res.status(200).json({
            success: true,
            data: groups
        });

    } catch (error) {
        console.error("Error fetching groups:", error);
        return res.status(500).json({
            success: false,
            message: "Failed to fetch groups",
            error: error.message
        });
    }
};

// Get group by ID
const getGroupById = async (req, res) => {
    try {
        const { id } = req.params;

        const group = await Group.findOne({
            where: { id },
            include: [{
                model: Member,
                through: GroupMember
            }]
        });

        if (!group) {
            return res.status(404).json({
                success: false,
                message: "Group not found"
            });
        }

        return res.status(200).json({
            success: true,
            data: group
        });

    } catch (error) {
        console.error("Error fetching group:", error);
        return res.status(500).json({
            success: false,
            message: "Failed to fetch group",
            error: error.message
        });
    }
};

// Update group
const updateGroup = async (req, res) => {
    try {
        const { id } = req.params;
        const { name, memberIds } = req.body;

        const group = await Group.findByPk(id);
        if (!group) {
            return res.status(404).json({
                success: false,
                message: "Group not found"
            });
        }

        // Update group name if provided
        if (name) {
            await group.update({ name });
        }

        // Update members if provided
        if (memberIds && Array.isArray(memberIds)) {
            // Check if all new members exist
            const members = await Member.findAll({
                where: {
                    id: memberIds
                }
            });

            if (members.length !== memberIds.length) {
                return res.status(404).json({
                    success: false,
                    message: "One or more members not found"
                });
            }

            // Remove existing members
            await GroupMember.destroy({
                where: { groupId: id }
            });

            // Add new members
            const groupMembers = memberIds.map(memberId => ({
                groupId: id,
                memberId: memberId
            }));

            await GroupMember.bulkCreate(groupMembers);
        }

        // Fetch updated group with member details
        const updatedGroup = await Group.findOne({
            where: { id },
            include: [{
                model: Member,
                through: GroupMember
            }]
        });

        return res.status(200).json({
            success: true,
            message: "Group updated successfully",
            data: updatedGroup
        });

    } catch (error) {
        console.error("Error updating group:", error);
        return res.status(500).json({
            success: false,
            message: "Failed to update group",
            error: error.message
        });
    }
};

// Delete group
const deleteGroup = async (req, res) => {
    try {
        const { id } = req.params;

        const group = await Group.findByPk(id);
        if (!group) {
            return res.status(404).json({
                success: false,
                message: "Group not found"
            });
        }

        // Delete group members first (due to foreign key constraints)
        await GroupMember.destroy({
            where: { groupId: id }
        });

        // Delete the group
        await group.destroy();

        return res.status(200).json({
            success: true,
            message: "Group deleted successfully"
        });

    } catch (error) {
        console.error("Error deleting group:", error);
        return res.status(500).json({
            success: false,
            message: "Failed to delete group",
            error: error.message
        });
    }
};

module.exports = {
    createGroup,
    getGroups,
    getGroupById,
    updateGroup,
    deleteGroup,
    getAllMembers
};