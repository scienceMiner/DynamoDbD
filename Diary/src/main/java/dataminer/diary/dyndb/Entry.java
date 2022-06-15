/*
 
   @author scienceMiner
   
*/

package dataminer.diary.dyndb;

import java.time.Instant;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;


/**
 * @author scienceMiner
 * DynamoDb Enhanced Client Bean class.
 */

@DynamoDbBean
public class Entry {

        private Integer id;
        private String name;
        private String entry;
        private Instant date;

        //@DynamoDbSortKey
        public Integer getId() {
            return this.id;
        };

        public void setId(Integer id) {

            this.id = id;
        }
    
        public String getUserName() {
            return this.name;
        }

        @DynamoDbPartitionKey
        public void setUserName(String name) {
            this.name = name;
        }

        public String getEntry() {
            return this.entry;
        }

        public void setEntry( String email ) {
            this.entry = email;
        }

        @DynamoDbSortKey
        public Instant getDate() {
            return date;
        }
        public void setDate(Instant entryDate) {

            this.date = entryDate;
        }
    }
