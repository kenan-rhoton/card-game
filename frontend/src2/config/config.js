config = require('./data/config.yml')

// Load server data with defaults if file doesn't exist
try {
    servers = require('./data/servers.yml')
} catch(error) {

    // Don't want to silently catch other errors
    if (error.code !== 'MODULE_NOT_FOUND') {
        throw error;
    }

    // Defaults to dev values
    servers = {
        servers: {
            frontend: 'localhost:8880',
            backend: 'localhost:3000'
        }
    }
}

module.exports = Object.assign(config,servers);