import React, { Component} from "react";
import "./Stage.scss";
import Navi from './Navi';
import Content from './Content';

export default
class Stage extends Component{
  
  constructor(props) {
    super(props);
    this.state = { 
      content: "home" 
    };
  };
  
  handleNaviClick(event) {
    this.setState({content: event.content});
  };
  
  render(){
    return(
      <div className="Stage">
        <div className="Stage-table">
          <Navi onNaviClick={(evt)=>this.handleNaviClick(evt)}/>
          <Content content={this.state.content}/>
        </div>
      </div>
    );
  };
}
