const Dotenv = require('dotenv-webpack')
const path = require('path')

module.exports = {
    entry: {
        main: './src/index.js',
        worker: './src/audio/worker.js'
    },
    devtool: 'eval-source-map',
    devServer: {
        contentBase: './dist',
        port: 9024,

        headers: {
            "Access-Control-Allow-Origin": "*",
            "Access-Control-Allow-Methods": "GET, POST, PUT, DELETE, PATCH, OPTIONS",
            "Access-Control-Allow-Headers": "X-Requested-With, content-type, Authorization"
        }
    },
    output: {
        filename: '[name].js',
        path: path.resolve(__dirname, 'dist'),
        publicPath: 'dist/'
    },
    mode: 'development',
    module: {
        rules: [{
            test: /\.css$/,
            use: ['style-loader', 'css-loader']
        },
            {
                test: /\.(jpe?g|png|gif)$/i,
                loader: 'file-loader',
                options: {
                    name: '[name].[ext]',
                    outputPath: 'assets/images/'
                }
            },
            {
                test: /\.m?js/,
                resolve: {
                    fullySpecified: false
                }
            }
        ]
    },
    plugins: [
        new Dotenv({
            path: path.resolve(__dirname,'..','.env'),
            systemvars: false,
            silent: false
        })
    ]
}
