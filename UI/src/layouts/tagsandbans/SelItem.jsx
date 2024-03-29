const SelItem = ({ word, addTagOrBan, type }) => {
  return (
    <div
      style={{
        border: "1px solid #bbb",
        color: "white",
        backgroundColor: type === 1 ? "#0088dd" : "#aa0000",
        padding: "2px",
        border: "1px solid #ddd",
        borderRadius: "5px",
        paddingLeft: "8px",
        paddingRight: "8px",
        margin: "2px",
        display: "inline-block",
        fontSize: "small",
        boxShadow: "0px 1px 1px rgba(0, 0, 0, 0.4)",
      }}
    >
      {word}
      <button
        style={{
          backgroundColor: "white",
          border: "none",
          color: "#aa0000",
          paddingLeft: "4px",
          paddingRight: "4px",
          paddingBottom: "2px",
          borderRadius: "50%",
          margin: "2px",
          marginLeft: "8px",
          cursor: "pointer",
          display: "inline-block",
          boxShadow: "2px 2px 4px rgba(0, 0, 0, 0.4)",
        }}
        onClick={() => addTagOrBan(word, type, -1)}
      >
        x
      </button>
    </div>
  );
};
export default SelItem;
