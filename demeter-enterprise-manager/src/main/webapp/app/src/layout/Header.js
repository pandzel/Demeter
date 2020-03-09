import React, { Component} from "react";
import "./Header.scss";

export default
class Header extends Component{
  render(){
    return(
      <div className="Header">
        <div><span className="Demeter">Demeter</span> Enterprise Server Manager ver. 2.1.0</div>
      </div>
    );
  }
}
