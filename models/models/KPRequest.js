const Sequelize = require("sequelize");
const db = require("../config/database.js");
const { DataTypes } = Sequelize;

const KPRequest = db.define('KPRequests', {
    id: {
        type: DataTypes.INTEGER,
        primaryKey: true,
        autoIncrement: true
    },
    groupId: {
        type: DataTypes.INTEGER,
        allowNull: false
    },
    company: {
        type: DataTypes.STRING,
        allowNull: false
    },
    startDate: {
        type: DataTypes.STRING,
        allowNull: false
    },
    endDate: {
        type: DataTypes.STRING,
        allowNull: false
    },
    proposalUrl: {
        type: DataTypes.STRING,
        allowNull: false
    },
    status: {
        type: DataTypes.STRING,
        defaultValue: 'Pending'
    },
    reason: {
        type: DataTypes.STRING
    }

}, {
    freezeTableName: true
});

module.exports = KPRequest;