var path    = require('path');
var hwp     = require('html-webpack-plugin');

module.exports = {
    entry: path.join(__dirname, '/src/index.js'),
    output: {
        filename: 'app.js',
        path: path.join(__dirname, '../js')
    },
    module:{
        rules:[
          {
            exclude: /node_modules/,
            test: /\.(js|jsx)$/,
            loader: 'babel-loader'
          },
          {
            test: /\.css$/,
            use: ["style-loader", "css-loader"]
          }
      ]
    },
    plugins:[
        new hwp({template:path.join(__dirname, '/src/index.html')})
    ]
}