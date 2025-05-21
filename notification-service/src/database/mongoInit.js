'use strict'

const mongoose = require('mongoose')
const {database:{host, name, port, user, password, appName}, debug} = require('../configs/appSettings')


const connectionString = `mongodb+srv://${user}:${password}@${host}/${name}?retryWrites=true&w=majority${appName}`



class Database {

    constructor() {
		console.log("Connection String: ",connectionString);

        this.connect()
    }

    connect(type = 'mongo') {
        if (debug === true) {
            mongoose.set('debug', true)
            mongoose.set('debug', {
                color: true
            })
        }

        mongoose.connect(
            connectionString
            ,{
                maxPoolSize:50
            }
        ).then(_ => {
            console.log('Connect MongoDB success.')
            // countConnect()
        }).catch(err => console.log(`Error when connect to database: ${err}`)
        )
    }

    static getInstance(){
        if(!Database.instance){
            Database.instance = new Database()
        }

        return Database.instance
    }
}

const instanceMongoDB = Database.getInstance()
module.exports = instanceMongoDB
