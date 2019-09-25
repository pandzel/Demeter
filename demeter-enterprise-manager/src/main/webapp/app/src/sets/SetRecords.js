import React, { Component} from "react";
import "./Sets.scss";
import {Button} from 'primereact/button';
import {Paginator} from 'primereact/paginator';
import SetsApi from '../api/SetsApi';

export default
class SetRecords extends Component{
  state = {
    data: {
      page: 0,
      pageSize: 0,
      total: 0,
      data: []
    }
  };
  
  api = new SetsApi();
  
  componentDidMount() {
    this.load();
  }
  
  load = (page) => {
    this.api.listRecords(this.props.currentSet.id, page).then(records => {
      console.log(records);
      this.setState({data: records});
    }).catch(this.props.onError);
  }
  
  render(){
    return(
      <div className="SetRecords">
        <div className="Title">{`Records included in "${this.props.currentSet.setName}"`}</div>
        <Paginator first={this.state.data.page * this.state.data.pageSize} rows={this.state.data.pageSize} totalRecords={this.state.data.total} onPageChange={(e) => this.load(e.page)}></Paginator>
        <Button label="Back" onClick={this.props.onExit}/>
      </div>
    );
  }
}
