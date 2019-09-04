import React, { Component} from "react";
import "./SetsPane.scss";
import SetsApi from '../api/SetsApi';
import SetsTable from './SetsTable';

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
  
  render(){
    return(
      <div className="SetsPane">
        <div className="Title">Sets</div>
        {this.state.data && <SetsTable data={this.state.data} onPageChange={this.load}/>}
      </div>
    );
  }
}
