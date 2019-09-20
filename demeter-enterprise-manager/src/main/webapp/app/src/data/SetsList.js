import React, { Component} from "react";
import "./DataPane.scss";
import {Button} from 'primereact/button';
import {Checkbox} from 'primereact/checkbox';
import SetsApi from '../api/SetsApi';

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
  
  api = new SetsApi();
  
  componentDidMount() {
    this.api.list().then(sets => {
      sets.data.forEach(set => {
        set.checked = !!this.props.sets.data.find(d=>d.key===set.id);
      });
      this.setState({sets});
    });
  }
  
  onCheck = (id, check) => {
    let action = check? this.api.putCollection: this.api.delCollection;
    action(id, this.props.record.id).then(result => {
      let newSets = {...this.state.sets};
      newSets.data.forEach(d => {
        if (d.id===id) {
          d.checked = check;
        }
      });
      this.setState({sets: newSets});
    });
  }

  render(){
    return(
      <div className="SetsList">
        <div className="Sets">
          {this.state.sets && this.state.sets.data.map(set => <Set key={set.id} set={set} onCheck={(check) => this.onCheck(set.id, check)}/>)}
        </div>
        <Button label="Cancel" onClick={(e) => this.props.onCancel()}/>
      </div>
    );
  }
}