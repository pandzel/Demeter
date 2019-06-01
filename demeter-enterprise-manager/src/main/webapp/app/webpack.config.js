const path                 = require('path');
const hwp                  = require('html-webpack-plugin');
const MiniCssExtractPlugin = require("mini-css-extract-plugin");

module.exports = (env,argv) => ({
    entry: path.join(__dirname, '/src/index.js'),
    output: {
        filename: 'app.js',
        path: path.join(__dirname, '../dist')
    },
    module:{
        rules:[
          {
            test: /\.(js|jsx)$/,
            exclude: /node_modules/,
            use: [
              {
                loader: 'babel-loader',
                options: {
                  presets: [ '@babel/preset-env', '@babel/react' ]
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
                  name: '[name].[ext]',
                  outputPath: 'images',
                  publicPath: (url, resourcePath, context) => {
                    if (argv && argv.mode == 'production') {
                      return `dist/images/${url}`;
                    } else {
                      return `images/${url}`;
                    }
                  }
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
});