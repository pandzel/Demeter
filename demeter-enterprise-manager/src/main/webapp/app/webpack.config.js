const path                 = require('path');
const hwp                  = require('html-webpack-plugin');
const MiniCssExtractPlugin = require("mini-css-extract-plugin");

module.exports = {
    entry: path.join(__dirname, '/src/index.js'),
    output: {
        filename: 'app.js',
        path: path.join(__dirname, '../dist')
    },
    module:{
        rules:[
          {
            exclude: /node_modules/,
            test: /\.(js|jsx)$/,
            use: [
              {
                loader: 'babel-loader',
                options: {
                  presets: [ 'es2017', 'react' ]
                }
              }
            ]
          },
          {
            test: /\.scss$/,
            use: [MiniCssExtractPlugin.loader, 'css-loader', 'sass-loader']
          },
          {
            test: /\.(png|svg|jpg|gif)$/,
            use: [
              {
                loader: 'file-loader',
                options: {
                  outputPath: 'images'
                }
              }
            ]
          }
      ]
    },
    plugins:[
      new MiniCssExtractPlugin({
        filename: 'style/main.css'
      }),
      new hwp({template:path.join(__dirname, '/src/index.html')})
    ]
}