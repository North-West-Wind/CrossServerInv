# Cross Server Inventory
Sync your inventory across servers. Idea from [InvSync](https://github.com/MrNavaStar/InvSync), but they still haven't updated to 1.20.1, so I decided to make my own.

## Configuration
Config file is located at `config/csi.json`:
```json
{
  "mysql_address": "",
  "mysql_port": 3306,
  "mysql_username": "",
  "mysql_password": "",
  "db_name": ""
}
```

## Building from source
You'll also need the MySQL Connector library if you want to build this project from source.  
Download the JDBC Driver [here](https://www.mysql.com/products/connector/) and place it in a directory `libs` at the root of this project.

After that, run `./gradlew build`.

## License
GPLv3