import React, { Component} from "react";
import "./Data.scss";
import {Button} from 'primereact/button';
import {Checkbox} from 'primereact/checkbox';
import SetsApi from '../api/SetsApi';

function Set(props) {
  return <div className="Set">
            <span className="CheckBox">
              <Checkbox inputId={props.set.id} onChange={e => props.onCheck(e.checked)} checked={props.set.checked}></Checkbox>
            </span>
            <span><label htmlFor={props.set.id}>{props.set.setName}</label></span>
         </div>;
};

export default
class SetsList extends Component {
  state = {
  };
  
  api = new SetsApi();
  
  componentDidMount() {
  }
  
  onCheck = (id, check) => {
  }

  render(){
    return(
      <div className="SetsList">
        <div className="Sets">
        </div>
        <Button label="Back" onClick={(e) => this.props.onExit()}/>
      </div>
    );
  }
}