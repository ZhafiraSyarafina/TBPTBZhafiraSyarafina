const Member = require('./Member');
const Group = require('./Group');
const GroupMember = require('./GroupMember');
const KPRequest = require('./KPRequest');

// Define relationships
Member.belongsToMany(Group, { through: GroupMember });
Group.belongsToMany(Member, { through: GroupMember });

Group.hasMany(KPRequest, { foreignKey: 'groupId' });
KPRequest.belongsTo(Group, { foreignKey: 'groupId' });


module.exports = {
    Member,
    Group,
    GroupMember,
    KPRequest,
};