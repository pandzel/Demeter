import React, {Component} from "react";
import "./DataPane.scss";
import {InputText} from 'primereact/inputtext';
import {Calendar} from 'primereact/calendar';
import {Button} from 'primereact/button';


function TextRow(props) {
  return <div className="table-row" style={{display: "table-row"}}>
          <div className="table-cell table-cell-caption" style={{display: "table-cell"}}>{props.caption}</div>
          <div className="table-cell table-cell-value"style={{display: "table-cell"}}>
            <InputText value={props.value} onChange={props.onChange}/>
          </div>
        </div>;
};

function DateRow(props) {
  return <div className="table-row" style={{display: "table-row"}}>
          <div className="table-cell table-cell-caption" style={{display: "table-cell"}}>{props.caption}</div>
          <div className="table-cell table-cell-value"style={{display: "table-cell"}}>
            <Calendar value={props.value? new Date(props.value): new Date()} onChange={props.onChange}/>
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
            <TextRow caption="Title" value={this.state.record.title}
                 onChange={(e) => this.setState({record: {title: e.target.value}})}/>
            <TextRow caption="Creator" value={this.state.record.creator}
                 onChange={(e) => this.setState({record: {creator: e.target.value}})}/>
            <TextRow caption="Subject" value={this.state.record.subject}
                 onChange={(e) => this.setState({record: {subject: e.target.value}})}/>
            <TextRow caption="Description" value={this.state.record.description}
                 onChange={(e) => this.setState({record: {description: e.target.value}})}/>
            <TextRow caption="Publisher" value={this.state.record.publisher}
                 onChange={(e) => this.setState({record: {publisher: e.target.value}})}/>
            <TextRow caption="Contributor" value={this.state.record.contributor}
                 onChange={(e) => this.setState({record: {contributor: e.target.value}})}/>
            <DateRow caption="Date" value={this.state.record.date}
                 onChange={(e) => this.setState({record: {date: e.value.toString()}})}/>
            <TextRow caption="Identifier" value={this.state.record.identifier}
                 onChange={(e) => this.setState({record: {identifier: e.target.value}})}/>
            <TextRow caption="Format" value={this.state.record.format}
                 onChange={(e) => this.setState({record: {format: e.target.value}})}/>
            <TextRow caption="Source" value={this.state.record.source}
                 onChange={(e) => this.setState({record: {source: e.target.value}})}/>
            <TextRow caption="Language" value={this.state.record.language}
                 onChange={(e) => this.setState({record: {language: e.target.value}})}/>
            <TextRow caption="Relation" value={this.state.record.relation}
                 onChange={(e) => this.setState({record: {relation: e.target.value}})}/>
            <TextRow caption="Coverage" value={this.state.record.coverage}
                 onChange={(e) => this.setState({record: {coverage: e.target.value}})}/>
            <TextRow caption="Rights" value={this.state.record.rights}
                 onChange={(e) => this.setState({record: {rights: e.target.value}})}/>
        </div>
        <Button label="Save" onClick={(e) => this.props.onSave(this.state.record)}/>
      </div>
    );
  }

}