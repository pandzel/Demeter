import React, {Component} from "react";
import "./Data.scss";
import {Checkbox} from 'primereact/checkbox';

export default 
class RecordSet extends Component {
  render(){
    return (<div className="RecordSet">
              <span className="CheckBox">
                <Checkbox inputId={this.props.set.id} onChange={e => this.props.onCheck(e.checked)} checked={this.props.set.checked}></Checkbox>
              </span>
              <span><label htmlFor={this.props.set.id}>{this.props.set.setName}</label></span>
            </div>
           );
  }
}
