import { Card } from "@mui/material";
import MDBox from "components/MDBox";
import MDTypography from "components/MDTypography";
import DashboardLayout from "examples/LayoutContainers/DashboardLayout";
import DashboardNavbar from "examples/Navbars/DashboardNavbar";
import Exp from "layouts/questions/components/Exp";
import Options from "layouts/questions/components/Options";
import { useEffect, useState } from "react";
import AxiosInstance from "scripts/axioInstance";
import SugItem from "./SugItem";
import SelItem from "./SelItem";
import Footer from "examples/Footer";
import { strlen } from "stylis";
import Loader from "layouts/loader/Loader";

function TagsAndBans() {
  const [mcqId, setMcqId] = useState();
  const [question, setQuestion] = useState();
  const [sugBans, setSugBans] = useState([]);
  const [sugTags, setSugTags] = useState([]);
  const [selTags, setSelTags] = useState([]);
  const [selBans, setSelBans] = useState([]);
  const [loading, setLoading] = useState(false);
  const [info, setInfo] = useState("");

  const fetchPending = async (url) => {
    AxiosInstance.get(url)
      .then((response) => {
        const id = response.data[0].targetId;
        console.log(response);
        setMcqId(id);
      })
      .catch((error) => {
        console.log(error);
      });
  };

  useEffect(() => {
    fetchPending("https://bcs-game.azurewebsites.net/batch/pending");
  }, []);

  const fetchData = async (url) => {
    AxiosInstance.get(url)
      .then((response) => {
        console.log(response);
        setQuestion(response.data);
      })
      .catch((error) => {
        console.log(error);
      });
  };

  useEffect(() => {
    fetchData("https://bcs-game.azurewebsites.net/unified?mcqId=" + mcqId);
    console.log("id updated to " + mcqId);
  }, [mcqId]);

  const fetchSuggestions = async (url) => {
    AxiosInstance.get(url)
      .then((response) => {
        console.log(" t and b");
        console.log(response);
        setSugBans(response.data.bans);
        setSugTags(response.data.tags);
      })
      .catch((error) => {
        console.log(error);
      });
  };

  useEffect(() => {
    fetchSuggestions("https://bcs-game.azurewebsites.net/util/suggest-tags/" + mcqId);
    console.log("suggestions " + mcqId);
  }, [mcqId]);

  const addTagOrBan = (word, type, action) => {
    const updateState = (newState, type) => {
      const existingItems = type === 1 ? selTags : selBans;
      const updatedState =
        action === 1
          ? [...new Set([...existingItems, word])]
          : existingItems.filter((item) => item !== word);

      if (type === 1) {
        setSelTags(updatedState);
      } else {
        setSelBans(updatedState);
      }
    };

    updateState(word, type);
  };

  const save = async () => {
    setLoading(true);

    const updated = {
      tagWords: selTags,
      bans: selBans,
    };

    const url = "https://bcs-game.azurewebsites.net/unified/" + question.id;

    try {
      const response = await AxiosInstance.put(url, updated);
      setInfo("Saved successfully.");
      setLoading(false);
      setTimeout(() => {
        window.location.reload();
      }, 1000);
    } catch (error) {
      console.error(error);
      setInfo("Error: " + error.message + " -> " + error.response.data);
      setLoading(false);
    }
  };

  return (
    <DashboardLayout>
      <DashboardNavbar />
      <div
        style={{
          maxWidth: "700px",
          margin: "auto",
        }}
      >
        {question !== undefined && (
          <Card key={question.id} style={{ marginBottom: "10px" }}>
            <MDBox pt={3} px={2}>
              <MDTypography variant="h6" fontWeight="medium">
                {question.id}. {question.question}
              </MDTypography>
            </MDBox>
            <MDBox pt={1} pb={1} px={2}>
              <MDBox component="ul" display="flex" flexDirection="column" p={0} m={0}>
                <Options
                  a={question.optionA}
                  b={question.optionB}
                  c={question.optionC}
                  d={question.optionD}
                  correct={question.answer}
                />
              </MDBox>
            </MDBox>
            <Exp mcq={question} />
            <div
              style={{
                margin: "5px",
                padding: "5px",
                borderRadius: "5px",
                backgroundColor: "#aa0000",
                fontWeight: "bold",
                color: "white",
              }}
            >
              SELECT BANS
            </div>
            {selBans !== undefined && selBans.length !== 0 && (
              <div
                display="flex"
                flexDirection="column"
                style={{
                  border: "1px solid #aa0000",
                  borderRadius: "10px",
                  padding: "10px",
                  margin: "5px",
                }}
              >
                {selBans.map((item, index) => {
                  return <SelItem key={index} word={item} addTagOrBan={addTagOrBan} type={2} />;
                })}
              </div>
            )}
            {sugBans !== undefined && (
              <div
                display="flex"
                flexDirection="column"
                style={{
                  border: "1px solid #aa0000",
                  borderRadius: "10px",
                  padding: "10px",
                  margin: "5px",
                }}
              >
                {sugBans.map((item, index) => {
                  return <SugItem key={index} word={item} addTagOrBan={addTagOrBan} type={2} />;
                })}
              </div>
            )}
            <div
              style={{
                margin: "5px",
                padding: "5px",
                borderRadius: "5px",
                backgroundColor: "#0088dd",
                color: "white",
                fontWeight: "bold",
              }}
            >
              SELECT TAGS
            </div>
            {selTags !== undefined && selTags.length !== 0 && (
              <div
                display="flex"
                flexDirection="column"
                style={{
                  justifyContent: "space-between",
                  border: "1px solid #0088dd",
                  borderRadius: "10px",
                  padding: "10px",
                  margin: "5px",
                }}
              >
                {selTags.map((item, index) => {
                  return <SelItem key={index} word={item} addTagOrBan={addTagOrBan} type={1} />;
                })}
              </div>
            )}
            {sugTags !== undefined && (
              <div
                display="flex"
                flexDirection="column"
                style={{
                  border: "1px solid #0088dd",
                  borderRadius: "10px",
                  padding: "10px",
                  margin: "5px",
                }}
              >
                {sugTags.map((item, index) => {
                  return <SugItem word={item.word} addTagOrBan={addTagOrBan} type={1} />;
                })}
              </div>
            )}
            <div style={{ display: "flex", width: "100%" }}>
              <input
                id="newTag"
                style={{
                  flex: 1,
                  border: "1px solid #0088dd",
                  padding: "10px",
                  margin: "5px",
                  borderRadius: "5px",
                  color: "#0088dd",
                }}
                placeholder="Tag don't exist in suggestion? Write a new one!"
              />
              <button
                style={{
                  backgroundColor: "#0088dd",
                  border: "1px solid #ccc",
                  padding: "10px",
                  margin: "5px",
                  borderRadius: "10px",
                  color: "white",
                  fontWeight: "bold",
                  cursor: "pointer",
                  boxShadow: "1px 1px 5px gray",
                }}
                onClick={(e) => {
                  const newTagInput = document.getElementById("newTag");
                  const val = newTagInput.value;
                  newTagInput.value = "";
                  if (val === null || val === undefined || strlen(val) === 0) {
                    return;
                  }
                  addTagOrBan(val, 1, 1);
                }}
              >
                Add new Tag
              </button>
            </div>
            {loading && <Loader />}
            {info !== undefined && strlen(info) !== 0 && (
              <dvi
                style={{
                  border: "2px solid #ccc",
                  padding: "5px",
                  margin: "5px",
                  borderRadius: "10px",
                  fontWeight: "bold",
                }}
              >
                {info}
              </dvi>
            )}
            <button
              style={{
                backgroundColor: "#333",
                border: "2px solid #ccc",
                padding: "10px",
                margin: "5px",
                borderRadius: "10px",
                color: "white",
                fontWeight: "bold",
                cursor: "pointer",
                boxShadow: "3px 3px 10px gray",
              }}
              onClick={save}
            >
              <h3>SAVE MCQ</h3>
            </button>
          </Card>
        )}
      </div>
      <Footer />
    </DashboardLayout>
  );
}

export default TagsAndBans;
