export default
class SetsApi {
  list() {
    var allSets = [];
    allSets.push({
      id: '0000000',
      setSpec: 'sample',
      setName: 'Sample set'
    });
    return new Promise((resolve, reject) => {
      resolve(allSets);
    });
  }
}