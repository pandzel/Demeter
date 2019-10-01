import React, { Component} from "react";
import "./Data.scss";
import {Button} from 'primereact/button';
import {Checkbox} from 'primereact/checkbox';
import SetsApi from '../api/SetsApi';
import DataApi from '../api/RecordsApi';

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
  
  setsApi = new SetsApi();
  dataApi = new DataApi();
  
  componentDidMount() {
    this.load();
  }
  
  load = (page) => {
    this.setsApi.list().then(allSets => {
      this.listSets().then(dataSets => {
        allSets.data.forEach(set => {
          set.checked = !!dataSets.find(ds => ds.key === set.id );
        });
        this.setState({sets: allSets});
      }).catch(this.props.onError);
    }).catch(this.props.onError);
  }
  
  listSets = (page) => {
    return new Promise((resolve, reject) => {
      this.dataApi.listSets(this.props.record.id, page).then(dataSets => {
        var data = dataSets.data;
        if (data && data.length >= dataSets.pageSize) {
          this.listSets(dataSets.page+1)
                  .then(result => resolve([...data, ...result.data]))
                  .catch(reject);
        } else {
          resolve(data);
        }
      }).catch(reject);
    });
  }
  
  onCheck = (id, check) => {
    let mutator = check? this.dataApi.putCollection: this.dataApi.delCollection;
    mutator(id, this.props.record.id).then(result => {
      let newSets = {...this.state.sets};
      newSets.data.forEach(d => {
        if (d.id===id) {
          d.checked = check;
        }
      });
      this.setState({sets: newSets});
    }).catch(this.props.onError);
  }

  render(){
    return(
      <div className="SetsList">
        <div className="Sets">
        {this.state.sets && this.state.sets.data.map(set => <Set key={set.id} set={set} onCheck={(check) => this.onCheck(set.id, check)}/>)}
        </div>
        <Button label="Back" onClick={(e) => this.props.onExit()}/>
      </div>
    );
  }
}