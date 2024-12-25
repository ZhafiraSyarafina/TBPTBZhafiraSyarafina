const Sequelize = require('sequelize');

const db = new Sequelize('logbookkp','root','',{
    host:"localhost",
    dialect:"mysql",
    logging: false 

});

module.exports = db; 
