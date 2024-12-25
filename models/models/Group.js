const Sequelize = require("sequelize");
const db = require("../config/database.js");
const { DataTypes } = Sequelize;

const Group = db.define('Groups', {
    id: {
        type: DataTypes.INTEGER,
        primaryKey: true,
        autoIncrement: true
    },
    name: {
        type: DataTypes.STRING,
        allowNull: false
    }
}, {
    freezeTableName: true
});

module.exports = Group;