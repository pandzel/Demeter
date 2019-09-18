import React, { Component} from "react";
import "./SetsPane.scss";
import SetsApi from '../api/SetsApi';
import SetsTable from './SetsTable';
import RecordsList from './RecordsList';

export default
class SetsPane extends Component{
  state = {};
  
  api = new SetsApi();
  
  componentDidMount() {
    this.load();
  }
  
  load = (page) => {
    return new Promise((resolve, reject) => {
      this.api.list(page).then(result => {
        this.setState({data: null},()=>{
          this.setState({ data: result },()=>resolve(result));
        });
      }).catch(reject);
    });
  }
  
  onInfo = (setId, records) => {
    this.setState({setId: setId, records: records});
  }
  
  onExit = () => {
    this.setState({records: null});
  }
  
  onRemove = (key) => {
    console.log("Removing", key);
    this.api.delCollection(this.state.setId, key).then(result => {
      this.api.listRecords(this.state.setId).then(result => this.setState({records: result}));
    });
  }
  
  render(){
    let body = this.state.data? <SetsTable ts={this.state.ts} data={this.state.data} onPageChange={this.load} onInfo={this.onInfo}/>: null;
    
    if (this.state.records) {
      body = <RecordsList cols={3} records={this.state.records} onExit={this.onExit} onRemove={this.onRemove} />
    }
    
    return(
      <div className="SetsPane">
        <div className="Title">Sets</div>
        {body}
      </div>
    );
  }
}
