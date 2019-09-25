import React, { Component} from "react";
import "./Sets.scss";
import {Button} from 'primereact/button';

export default
class SetRecords extends Component{
  state = {};
  
  componentDidMount() {
  }
  
  render(){
    return(
      <div className="SetRecords">
        <div className="Title">Set Records</div>
        <Button label="Back" onClick={this.props.onExit}/>
      </div>
    );
  }
}
