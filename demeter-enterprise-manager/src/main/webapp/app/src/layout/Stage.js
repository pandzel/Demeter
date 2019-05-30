import React, { Component} from "react";
import "./Stage.scss";
import Navi from './Navi'

export default
class Stage extends Component{
  
  constructor(props) {
    super(props);
    
    this.handleNaviClick = this.handleNaviClick.bind(this);
  };
  
  handleNaviClick(e) {
    console.log("Navi clicked.", e);
  };
  
  render(){
    return(
      <div className="Stage">
        <Navi onNaviClick={this.handleNaviClick}/>
        <div></div>
      </div>
    );
  };
}
