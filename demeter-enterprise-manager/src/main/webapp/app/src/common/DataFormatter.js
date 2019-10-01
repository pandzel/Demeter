import React from "react";

function formatData(data, cols, callback) {
  if (!data) return null;
  
  const COLS = cols || 2;
  
  let rowsOfData = data.reduce((acc,record,idx) => {
    let pos = Math.floor(idx/COLS);
    while (acc.length<=pos)
      acc.push([]);
    acc[pos].push(record);
    return acc;
  }, []);
  
  let rowsOfRecords = rowsOfData.map((row, ridx) => 
    <div key={ridx} style={{display: "table-row"}}>
      {row.map(record => (
        <div key={record.key} className="recordCell" style={{display: "table-cell"}}>
          {callback(record)}
        </div>
      ))}
    </div>
  );
  
  return rowsOfRecords;
}

export default formatData;