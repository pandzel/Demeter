import React, { Component} from "react";
import "./DataPane.scss";
import {Button} from 'primereact/button';

export default
class DataPane extends Component{
  state = {};

  render(){
    return(
      <div className="SetsList">
        <div>
          Sets List
        </div>
        <Button label="Cancel" onClick={(e) => this.props.onCancel()}/>
      </div>
    );
  }
}