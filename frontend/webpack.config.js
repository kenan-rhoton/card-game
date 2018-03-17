const path = require('path');
const HtmlWebpackPlugin = require('html-webpack-plugin')
const CleanWebpackPlugin = require('clean-webpack-plugin');

module.exports = {
    mode: "development",
    entry: {
        lobby_create: './src/lobby/create/create.js',
        lobby_waiting: './src/lobby/waiting/waiting.js',
        lobby_join: './src/lobby/join/join.js',
        game: './src/game/game.js'
    },
    devtool: 'inline-source-map',
    devServer: {
        allowedHosts: [
            "localhost",
            "frontend",
            ".rhoton.es"
        ],
        host: "0.0.0.0",
        port: 8880,
        contentBase: './dist'
    },
    output: {
        filename: '[name].bundle.js',
        path: path.resolve(__dirname, 'dist')
    },
    resolve: {
        modules: [path.resolve(__dirname, "src"), "node_modules"],
        extensions: ['.js', '.jsx', '.json']
    },
    module: {
        rules: [
            {
                test: /\.pug$/,
                use: {
                    loader: 'pug-loader',
                    options: {}
                }
            },
            {
                test: /\.yml$/,
                use: [
                    { loader: 'json-loader' },
                    { loader: 'yaml-loader' }
                ]
            }
        ]
    },
    plugins: [
        new CleanWebpackPlugin(['dist']),
        new HtmlWebpackPlugin({
            filename: 'index.html',
            template: './src/index.pug',
            chunks: []
        }),
        new HtmlWebpackPlugin({
            filename: 'lobby_create.html',
            template: './src/lobby/create/create.pug',
            chunks: ['lobby_create']
        }),
        new HtmlWebpackPlugin({
            filename: 'lobby_waiting.html',
            template: './src/lobby/waiting/waiting.pug',
            chunks: ['lobby_waiting']
        }),
        new HtmlWebpackPlugin({
            filename: 'lobby_join.html',
            template: './src/lobby/join/join.pug',
            chunks: ['lobby_join']
        }),
        new HtmlWebpackPlugin({
            filename: 'game.html',
            template: './src/game/game.pug',
            chunks: ['game']
        }),
    ]
}
