import React, { Component} from "react";
import "./App.scss";

import Header from "./layout/Header";
import Stage  from "./layout/Stage";
import Footer from "./layout/Footer";

export default
class App extends Component{
  render(){
    return(
      <div className="App">
        <Header/>
        <Stage/>
        <Footer/>
      </div>
    );
  }
}
