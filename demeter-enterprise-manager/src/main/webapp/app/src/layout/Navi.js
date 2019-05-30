import React, { Component} from "react";
import "./Navi.scss";

export default
class Navi extends Component{
  
  constructor(props) {
    super(props);
    
    this.handleClick = this.handleClick.bind(this);
  };
  
  handleClick(e) {
    e.preventDefault();
    this.props.onNaviClick({choice: 1});
  };
  
  render(){
    return(
      <div className="Navi" onClick={this.handleClick}>
        <div></div>
      </div>
    );
  };
}
