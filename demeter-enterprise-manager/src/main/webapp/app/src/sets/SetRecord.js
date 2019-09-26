import React, { Component} from "react";
import "./Sets.scss";
import {Button} from 'primereact/button';

export default
class SetRecord extends Component{
  render(){
    return(<div className="Record">
            <span>
              <Button type="button" icon="pi pi-times-circle"
                      title="Remove"
                      onClick={() => this.props.onRemove(this.props.record.key)}/>
            </span>
            <span>{this.props.record.value}</span>
          </div>
    );
  }
}
