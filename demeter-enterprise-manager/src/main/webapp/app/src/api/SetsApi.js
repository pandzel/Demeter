import axios from 'axios';

export default
class SetsApi {
  list() {
    return new Promise((resolve, reject) => {
      axios.get(`${config.context}rest/sets`)
              .then((result)=>resolve(result.data.data))
              .catch((error)=>reject(error));
    });
  }
  
  create(data) {
    return new Promise((resolve, reject) => {
      axios.post(`${config.context}rest/sets`, data==undefined? {}: data)
              .then((result)=>resolve(result.data))
              .catch((error)=>reject(error));
    });
  }
  
  delete(id) {
    return new Promise((resolve, reject) => {
      axios.delete(`${config.context}rest/sets/${id}`)
              .then((result)=>resolve(result.data))
              .catch((error)=>reject(error));
    });
  }
  
  update(data) {
    return new Promise((resolve, reject) => {
      axios.put(`${config.context}rest/sets/${data.id}`, data)
              .then((result)=>resolve(result.data))
              .catch((error)=>reject(error));
    });
  }
}