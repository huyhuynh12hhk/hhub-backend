import {
  Box,
  Card,
  IconButton,
  List,
  ListItem,
  ListItemText,
  Stack,
  TextField,
} from "@mui/material";
import { grey } from "@mui/material/colors";
import { useState } from "react";
import SearchIcon from '@mui/icons-material/Search';
import Grid from "@mui/material/Grid2";
import { notifyNotImplementFeatureMessage } from "../../utils/toastHelper";

const data = ["Apple", "Banana", "Cherry", "Date", "Grapes", "Mango", "Orange"];

const SearchBar = () => {
  const [searchTerm, setSearchTerm] = useState("");

  const filteredData = data.filter((item) =>
    item.toLowerCase().includes(searchTerm.toLowerCase())
  );

  return (
    <Grid container width='50%' maxWidth='500px' minWidth='300px' mx="auto"  >
      {/* Search Input */}
      <TextField
        sx={{
            width:'80%'
        }}
        size="small"
        variant="outlined"
        value={searchTerm}
        placeholder="Search something here..."
        onChange={(e) => setSearchTerm(e.target.value)}
      />
    <IconButton onClick={()=>notifyNotImplementFeatureMessage()}>
        <SearchIcon color="info"/>
    </IconButton>
      {/* Search Results */}
      {/* {searchTerm && filteredData.length > 0 && (
        <Card
            position="absolute"
            
            width={500}
            sx={{
                backgroundColor:grey,
                bottom:'30px'
            }}
        >
          <List>
            {filteredData.map((item, index) => (
              <ListItem key={index}>
                <ListItemText primary={item} />
              </ListItem>
            ))}
          </List>
        </Card>
      )} */}
    </Grid>
  );
};

export default SearchBar;
