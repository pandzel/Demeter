import React, {Component} from "react";
import "./DataPane.scss";
import {InputText} from 'primereact/inputtext';

function Row(props) {
  return <div className="table-row" style={{display: "table-row"}}>
          <div className="table-cell table-cell-caption" style={{display: "table-cell"}}>{props.caption}</div>
          <div className="table-cell table-cell-value"style={{display: "table-cell"}}>
            <InputText type="text" value={props.value} onChange={props.onChange}/>
          </div>
        </div>;
};

export default
class EditorPane extends Component {
  state  = { 
    record: this.props.record
  };
  
  render(){
    return(
      <div className="EditorPane">
        <div className="table" style={{display: "table"}}>
            <Row caption="Title" value={this.state.record.title}
                 onChange={(e) => this.setState({record: {title: e.target.value}})}/>
            <Row caption="Description" value={this.state.record.description}
                 onChange={(e) => this.setState({record: {description: e.target.value}})}/>
        </div>
      </div>
    );
  }

}