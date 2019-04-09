<!DOCTYPE html>
<html>
    <head>
        <title>Demeter Generic Server</title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <style>
          .stage {
            width: 600px;
            margin-left: auto;
            margin-right: auto;          
          }
          .stage > div {
            margin-bottom: 20px;
          }
          .stage .info {
            background-color: lightcyan;
            font-family: monospace;
          }
          .stage .explanation {
            
          }
          .stage h3 {
            background-color: lightblue;
          }
        </style>
    </head>
    <body>
      <div class="stage">
        <div>
          <h1>Welcome to Demeter</h1>
        </div>
        <div>
          This is a generic version of the OAI-PMH server. Please, customize content of this page.
        </div>
        <div>
          <h3>Environment:</h3>
          Your root folder is: <span class="info">${rootFolder}</span>.<br>
          <span class="explanation">Copy any of your metadata to this folder to make it available.</span>
          <p/>
          Your identify configuration file is: <span class="info">${configFile}</span><br>
          <span class="explanation">Update this file for 'Identify' request.</span>
          <p/>
          Your service configuration file is: <span class="info">${propFile}</span><br>
          <span class="explanation">Make further modifications or leave as is.</span>
          <p/>
          Please, note that any change to the configuration file or to the content of the root folder
          will be updated after next time the server is restarted.
        </div>
        <div>
          <h3>Sample requests:</h3>
          Identify repository: <a href="oai?verb=Identify" target="_blank">oai?verb=Identify</a><br>
          List metadata formats: <a href="oai?verb=ListMetadataFormats" target="_blank">oai?verb=ListMetadataFormats</a><br>
          List identifiers: <a href="oai?verb=ListIdentifiers&metadataPrefix=oai_dc" target="_blank">oai?verb=ListIdentifiers&metadataPrefix=oai_dc</a><br>
          List records: <a href="oai?verb=ListIdentifiers&metadataPrefix=oai_dc" target="_blank">oai?verb=ListRecords&metadataPrefix=oai_dc</a><br>
        </div>
      </div>
    </body>
</html>
