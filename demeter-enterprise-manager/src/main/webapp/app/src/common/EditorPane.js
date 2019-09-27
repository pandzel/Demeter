import React, {Component} from "react";
import "./EditorPane.scss";
import {InputText} from 'primereact/inputtext';
import {Calendar} from 'primereact/calendar';
import {Button} from 'primereact/button';
import DataApi from '../api/RecordsApi';


function TextRow(props) {
  return <div className="table-row" style={{display: "table-row"}}>
          <div className="table-cell table-cell-caption" style={{display: "table-cell"}}>{props.caption}</div>
          <div className="table-cell table-cell-value" style={{display: "table-cell"}}>
            <InputText value={props.value} onChange={props.onChange} style={{width: props.width}}/>
          </div>
        </div>;
};

function DateRow(props) {
  return <div className="table-row" style={{display: "table-row"}}>
          <div className="table-cell table-cell-caption" style={{display: "table-cell"}}>{props.caption}</div>
          <div className="table-cell table-cell-value" style={{display: "table-cell"}}>
            <Calendar value={props.value? new Date(props.value): new Date()} onChange={props.onChange}/>
          </div>
        </div>;
};

export default
class EditorPane extends Component {
  state  = { 
    record: this.props.record,
    modified: false
  };
  
  api = new DataApi();
  
  onSave = (record) => {
    this.api.update(record).then(response => {
      this.setState({modified: false});
    }).catch(this.props.onError);
  }
  
  render(){
    return(
      <div className="EditorPane">
        <div className="table" style={{display: "table"}}>
            <TextRow caption="Title" value={this.state.record.title} width={"60em"}
                 onChange={(e) => this.setState({record: {...this.state.record, title: e.target.value}, modified: true})}/>
            <TextRow caption="Description" value={this.state.record.description} width={"60em"}
                 onChange={(e) => this.setState({record: {...this.state.record, description: e.target.value}, modified: true})}/>
            <TextRow caption="Identifier" value={this.state.record.identifier} width={"60em"}
                 onChange={(e) => this.setState({record: {...this.state.record, identifier: e.target.value}, modified: true})}/>
            <TextRow caption="Subject" value={this.state.record.subject} width={"60em"}
                 onChange={(e) => this.setState({record: {...this.state.record, subject: e.target.value}, modified: true})}/>
            <TextRow caption="Creator" value={this.state.record.creator}
                 onChange={(e) => this.setState({record: {...this.state.record, creator: e.target.value}, modified: true})}/>
            <TextRow caption="Publisher" value={this.state.record.publisher}
                 onChange={(e) => this.setState({record: {...this.state.record, publisher: e.target.value}, modified: true})}/>
            <TextRow caption="Contributor" value={this.state.record.contributor}
                 onChange={(e) => this.setState({record: {...this.state.record, contributor: e.target.value}, modified: true})}/>
            <DateRow caption="Date" value={this.state.record.date}
                 onChange={(e) => this.setState({record: {...this.state.record, date: e.value.toString()}, modified: true})}/>
            <TextRow caption="Format" value={this.state.record.format}
                 onChange={(e) => this.setState({record: {...this.state.record, format: e.target.value}, modified: true})}/>
            <TextRow caption="Source" value={this.state.record.source}
                 onChange={(e) => this.setState({record: {...this.state.record, source: e.target.value}, modified: true})}/>
            <TextRow caption="Language" value={this.state.record.language}
                 onChange={(e) => this.setState({record: {...this.state.record, language: e.target.value}, modified: true})}/>
            <TextRow caption="Relation" value={this.state.record.relation}
                 onChange={(e) => this.setState({record: {...this.state.record, relation: e.target.value}, modified: true})}/>
            <TextRow caption="Coverage" value={this.state.record.coverage}
                 onChange={(e) => this.setState({record: {...this.state.record, coverage: e.target.value}, modified: true})}/>
            <TextRow caption="Rights" value={this.state.record.rights}
                 onChange={(e) => this.setState({record: {...this.state.record, rights: e.target.value}, modified: true})}/>
        </div>
        <Button label="Save" onClick={(e) => this.onSave(this.state.record)} disabled={!this.state.modified}/>
        <Button label="Back" onClick={(e) => this.props.onExit(this.props.page)}/>
      </div>
    );
  }

}