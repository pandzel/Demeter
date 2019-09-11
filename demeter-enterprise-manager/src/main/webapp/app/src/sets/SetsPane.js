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
  
  onInfo = (records) => {
    this.setState({records: records});
  }
  
  onExit = () => {
    this.setState({records: null});
  }
  
  render(){
    let body = null;
    
    if (this.state.records) {
      body = <RecordsList data={this.state.records} onExit={this.onExit} />
    } else if (this.state.data) {
      body = <SetsTable data={this.state.data} onPageChange={this.load} onInfo={this.onInfo}/>;
    }
    
    return(
      <div className="SetsPane">
        <div className="Title">Sets</div>
        {body}
      </div>
    );
  }
}
