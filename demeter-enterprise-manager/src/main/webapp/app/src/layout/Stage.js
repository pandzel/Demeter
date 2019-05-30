import React, { Component} from "react";
import "./Stage.scss";
import Navi from './Navi'

export default
class Stage extends Component{
  render(){
    return(
      <div className="Stage">
        <Navi/>
        <div></div>
      </div>
    );
  }
}
