package dataminer.diary.dyndb;

import java.math.BigInteger;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dataminer.diary.CacheHandler;
import dataminer.diary.EntryType;
import dataminer.diary.DateUtils;
import dataminer.diary.ui.DiaryGUI;
//snippet-start:[dynamodb.java2.create_table.import]
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.core.waiters.WaiterResponse;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.BatchWriteItemEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.WriteBatch;
import software.amazon.awssdk.enhanced.dynamodb.model.WriteBatch.Builder;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeDefinition;
import software.amazon.awssdk.services.dynamodb.model.CreateTableRequest;
import software.amazon.awssdk.services.dynamodb.model.CreateTableResponse;
import software.amazon.awssdk.services.dynamodb.model.DeleteTableRequest;
import software.amazon.awssdk.services.dynamodb.model.DescribeTableRequest;
import software.amazon.awssdk.services.dynamodb.model.DescribeTableResponse;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;
import software.amazon.awssdk.services.dynamodb.model.KeySchemaElement;
import software.amazon.awssdk.services.dynamodb.model.KeyType;
import software.amazon.awssdk.services.dynamodb.model.ProvisionedThroughput;
import software.amazon.awssdk.services.dynamodb.model.ScalarAttributeType;
import software.amazon.awssdk.services.dynamodb.model.TableDescription;
import software.amazon.awssdk.services.dynamodb.waiters.DynamoDbWaiter;
//snippet-end:[dynamodb.java2.create_table.import]

/**
* Before running this Java V2 code example, set up your development environment, including your credentials.
*
* For more information, see the following documentation topic:
*
* https://docs.aws.amazon.com/sdk-for-java/latest/developer-guide/get-started.html
*/
public class EntryMigration {

	private static Logger logger =  LogManager.getLogger( EntryMigration.class );

	public static String tableName = "Entry";
	public static Integer currentMaxId = 0;
	
	private DynamoDbClient ddb = null;
	private DynamoDbEnhancedClient enhancedClient = null;
     
  public static void main(String[] args) {

	  EntryMigration em1 = new EntryMigration();
      final String usage = "\n" +
              "Usage:\n" +
              "    <tableName> <key>\n\n" +
              "Where:\n" +
              "    tableName - The Amazon DynamoDB table to create (for example, Music3).\n\n" +
              "    key - The key for the Amazon DynamoDB table (for example, Artist).\n" ;

     if (args.length != 2) {
          System.out.println(usage);
          System.exit(1);
     }

      String tableName = args[0];
      String key = args[1];
      
      logger.debug("Creating an Amazon DynamoDB table "+tableName +" with a simple primary key: " +key );
       
      DynamoDbClient ddb = initializeDDBClient();
      DynamoDbEnhancedClient enhancedClient = initializeEnhancedClient(ddb);

  //    createTableWithoutIndex(enhancedClient);
      
  //    System.out.println(" New table created " );
      
  //    String entryResult = putRecord(enhancedClient);
      	
      // getItem(enhancedClient);
    //   getTableSize(ddb , tableName );
      
   	putBatchRecords(enhancedClient);
      
 //      scan(enhancedClient);
       
    //  deleteDynamoDBTable(ddb,"Entry");
      
    //   close();
      
  }
  
public static DynamoDbClient initializeDDBClient() {
	  
	  ProfileCredentialsProvider credentialsProvider = ProfileCredentialsProvider.create();
      Region region = Region.EU_NORTH_1;
      
      DynamoDbClient ddb = DynamoDbClient.builder()
              .credentialsProvider(credentialsProvider)
              .region(region)
              .build();
      
      return ddb;
}
  
  public static DynamoDbEnhancedClient initializeEnhancedClient(DynamoDbClient ddb ) {
	  
      DynamoDbEnhancedClient enhancedClient = DynamoDbEnhancedClient.builder()
              .dynamoDbClient(ddb)
              .build();
      
      return enhancedClient;
  }
  
  public static void createTableWithoutIndex( DynamoDbEnhancedClient edb )
  {
	  
	  DynamoDbTable<Entry> custTable = edb.table(tableName, TableSchema.fromBean(Entry.class));

	  custTable.createTable();
	  
  }
  
  public static void deleteDynamoDBTable(DynamoDbClient ddb, String tableName) {

      DeleteTableRequest request = DeleteTableRequest.builder()
              .tableName(tableName)
              .build();

      try {
          ddb.deleteTable(request);

      } catch (DynamoDbException e) {
          System.err.println(e.getMessage());
          System.exit(1);
      }
      logger.debug(tableName +" was successfully deleted!");
  }
  

  public static String createTable(DynamoDbClient ddb, String tableName, String key) {

      DynamoDbWaiter dbWaiter = ddb.waiter();
      CreateTableRequest request = CreateTableRequest.builder()
              .attributeDefinitions(AttributeDefinition.builder()
                      .attributeName(key)
                      .attributeType(ScalarAttributeType.S)
                      .build())
              .keySchema(KeySchemaElement.builder()
                      .attributeName(key)
                      .keyType(KeyType.HASH)
                      .build())
              .provisionedThroughput(ProvisionedThroughput.builder()
                      .readCapacityUnits(new Long(10))
                      .writeCapacityUnits(new Long(10))
                      .build())
              .tableName(tableName)
              .build();

      String newTable ="";
      try {
          CreateTableResponse response = ddb.createTable(request);
          DescribeTableRequest tableRequest = DescribeTableRequest.builder()
                  .tableName(tableName)
                  .build();

          // Wait until the Amazon DynamoDB table is created.
          WaiterResponse<DescribeTableResponse> waiterResponse = dbWaiter.waitUntilTableExists(tableRequest);
          waiterResponse.matched().response().ifPresent(System.out::println);
          newTable = response.tableDescription().tableName();
          return newTable;

      } catch (DynamoDbException e) {
          System.err.println(e.getMessage());
          System.exit(1);
      }
     return "";
  }

  public static String putRecord(DynamoDbEnhancedClient enhancedClient, String userName, EntryType eType) {

	  currentMaxId++;
	  
      try {
    	     	  
          DynamoDbTable<Entry> entryTable = enhancedClient.table(tableName, TableSchema.fromBean(Entry.class));

          // Create an Instant value.
         Instant instant = DateUtils.createInstantDate(eType);

          // Populate the Table.
          Entry userRecord = new Entry();
          userRecord.setUserName(userName);
          userRecord.setId(currentMaxId);
          userRecord.setEntry(eType.getValue());
          userRecord.setDate(instant) ;

          // Put the customer data into an Amazon DynamoDB table.
          entryTable.putItem(userRecord);

      } catch (DynamoDbException e) {
    	  currentMaxId--;
          System.err.println(e.getMessage());
          System.exit(1);
      }
      
      return "Customer data added to the table.";
      
  }
  
  
  public static void putBatchRecords(DynamoDbEnhancedClient enhancedClient) {

      try {
          DynamoDbTable<Entry> mappedTable = enhancedClient.table(tableName, TableSchema.fromBean(Entry.class));
          
          String fileNamePath = DiaryGUI.initialFileNamePath;
  		  CacheHandler cacheHandler = new CacheHandler();
  		  List<EntryType> entryTypeList = cacheHandler.populateEntryList(fileNamePath);
  		
  		  ArrayList<Entry>  entryList = new ArrayList<Entry>();
        
  		  Builder<Entry> builderEntry1 = WriteBatch.builder(Entry.class)
				.mappedTableResource(mappedTable);
       		
  		  int count=1;
  		  for (EntryType e : entryTypeList) {
  			  Entry record = new Entry();
  			  record.setUserName("ucacerc");
  			  
  			  StringBuilder sb = new StringBuilder();
  			  record.setId(count++);
  			  record.setEntry(e.getValue());
  			  Instant i = createInstant(e.getDay(),e.getMonth(),e.getYear());
  			  record.setDate(i) ;

  			builderEntry1.addPutItem( r -> r.item(record) );    
  			if (count % 20 == 0) {
  			  BatchWriteItemEnhancedRequest batchWriteItemEnhancedRequest =
  	                  BatchWriteItemEnhancedRequest.builder()
  	                          .writeBatches(builderEntry1.build())
  	                          .build();
  			  enhancedClient.batchWriteItem(batchWriteItemEnhancedRequest); 	         
  			logger.info(" wrote 20 records ");
  			  builderEntry1 = WriteBatch.builder(Entry.class)
  					.mappedTableResource(mappedTable);
  	       		
  			}
  		  }
  		
   
          
          // Add these two items to the table.
 //         enhancedClient.batchWriteItem(batchWriteItemEnhancedRequest);
  		logger.debug("done");

          
      } catch (DynamoDbException e) {
          System.err.println(e.getMessage());
          System.exit(1);
      }
  }


  
//snippet-start:[dynamodb.java2.mapping.getitem.main]
  public static void getItem(DynamoDbEnhancedClient enhancedClient) {

      try {
          DynamoDbTable<Entry> table = enhancedClient.table(tableName, TableSchema.fromBean(Entry.class));
          Key key = Key.builder()
                  .partitionValue("119")
                  .build();

          // Get the item by using the key.
          Entry result = table.getItem(r -> r.key(key));
          logger.debug("******* The description value is "+result.getUserName());

      } catch (DynamoDbException e) {
          System.err.println(e.getMessage());
          System.exit(1);
      }
  }
  
//snippet-start:[dynamodb.java2.mapping.scan.main]
  public static List<EntryType> scan( DynamoDbEnhancedClient enhancedClient) {

	  List<EntryType> entryTypeList = new ArrayList<EntryType>();
	  
	  
      try{
          DynamoDbTable<Entry> custTable = enhancedClient.table(tableName, TableSchema.fromBean(Entry.class));
          Iterator<Entry> results = custTable.scan().items().iterator();
          while (results.hasNext()) {

              Entry rec = results.next();
            //  System.out.println( "The record id is "+rec.getId() + "The name is " + rec.getCustName() );
              EntryType eType = new EntryType();
              
              ZoneId z = ZoneId.of( "Europe/London" );
              ZonedDateTime zdt = rec.getDate().atZone(z);

              eType.setDay(BigInteger.valueOf(zdt.getDayOfMonth()));
              eType.setMonth(zdt.getMonth().getDisplayName(TextStyle.SHORT,  Locale.ENGLISH));      
              eType.setYear( BigInteger.valueOf(zdt.getYear() ) );
              eType.setValue(rec.getEntry());
              
              if ( rec!= null && rec.getId() > currentMaxId) {
            	  currentMaxId = rec.getId();
              }
              entryTypeList.add(eType);
          }

      } catch (DynamoDbException e) {
          System.err.println(e.getMessage());
          System.exit(1);
      }
      logger.info("Done, currentId: " + currentMaxId );
      
      return entryTypeList;
  }
  
  
  
  public static Instant createInstant(BigInteger day, String month, BigInteger year ) {
	  logger.debug(" month: " + month + " day " + day);
	  StringBuilder sb = new StringBuilder().append(year).append("-").append(DateUtils.mapMonthTo2DigitString(month))
			  .append("-").append(DateUtils.mapDigitTo2DigitString(day));
	  LocalDate localDate = LocalDate.parse(sb.toString());
      LocalDateTime localDateTime = localDate.atStartOfDay();
      Instant instant = localDateTime.toInstant(ZoneOffset.UTC);

	  return instant;
  }
  
  public static Integer getTableSize( DynamoDbClient ddb, String tableName ) {

      DescribeTableRequest request = DescribeTableRequest.builder()
              .tableName(tableName)
              .build();

      int size = 0;
      try {
          TableDescription tableInfo =
                  ddb.describeTable(request).table();

          if (tableInfo != null) {
        	  size =  tableInfo.itemCount().intValue();
              logger.info( "Item count  : " +  size );
              
          }
      } catch (DynamoDbException e) {
          System.err.println(e.getMessage());
          System.exit(1);
      }
      
      return size;
  }
 
  
}