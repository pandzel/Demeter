import axios from 'axios';

export default
class SetsApi {
  list(page) {
    return new Promise((resolve, reject) => {
      axios.get(`${config.context}rest/sets`+(page!==undefined? `?page=${page}`: ""))
              .then((result)=>{ console.log("SetsApi list():", result.data); resolve(result.data); })
              .catch((error)=>{ console.error(error); reject(error); });
    });
  }
  
  create(data) {
    return new Promise((resolve, reject) => {
      axios.post(`${config.context}rest/sets`, data==undefined? {}: data)
              .then((result)=>{ console.log("SetsApi create():", result.data); resolve(result.data); })
              .catch((error)=>{ console.error(error); reject(error); });
    });
  }
  
  delete(id) {
    return new Promise((resolve, reject) => {
      axios.delete(`${config.context}rest/sets/${id}`)
              .then((result)=>{ console.log("SetsApi delete():", result.data); resolve(result.data); })
              .catch((error)=>{ console.error(error); reject(error); });
    });
  }
  
  update(data) {
    return new Promise((resolve, reject) => {
      axios.put(`${config.context}rest/sets/${data.id}`, data)
              .then((result)=>{ console.log("SetsApi update():", result.data); resolve(result.data); })
              .catch((error)=>{ console.error(error); reject(error); });
    });
  }
  
  listRecords(setId, page) {
    return new Promise((resolve, reject) => {
      axios.get(`${config.context}rest/sets/${setId}/records`+(page!==undefined? `?page=${page}`: ""))
              .then((result)=>{ console.log("SetsApi listRecords():", result.data); resolve(result.data); })
              .catch((error)=>{ console.error(error); reject(error); });
    });
  }
  
  putCollection(setId, recordId) {
    return new Promise((resolve, reject) => {
      axios.put(`${config.context}rest/sets/${setId}/records/${recordId}`)
              .then((result)=>{ console.log("SetsApi putCollection():", result.data); resolve(result.data); })
              .catch((error)=>{ console.error(error); reject(error); });
    });
  }
  
  delCollection(setId, recordId) {
    return new Promise((resolve, reject) => {
      axios.delete(`${config.context}rest/sets/${setId}/records/${recordId}`)
              .then((result)=>{ console.log("SetsApi putCollection():", result.data); resolve(result.data); })
              .catch((error)=>{ console.error(error); reject(error); });
    });
  }
}