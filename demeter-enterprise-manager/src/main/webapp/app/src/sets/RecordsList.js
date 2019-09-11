import React, { Component} from "react";
import "./SetsPane.scss";
import {Button} from 'primereact/button';

export default
class RecordsList extends Component {
  state = {};

  render(){
    return(
      <div className="SetsList">
        <div>
          Records List
        </div>
        <Button label="Cancel" onClick={(e) => this.props.onExit()}/>
      </div>
    );
  }
}