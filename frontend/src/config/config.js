try {
    var config = require('./data/config.yml');
} catch(error) {
    if (error.code !== "MODULE_NOT_FOUND") {
        throw error;
    }
    config = {
        servers: {
            backend: "potato",
        }
    };
}

module.exports =  config;
