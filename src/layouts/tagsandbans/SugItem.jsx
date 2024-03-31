const SugItem = ({ word, addTagOrBan, type }) => {
  return (
    <div
      style={{
        border: "1px solid #bbb",
        color: "black",
        padding: "2px",
        border: "1px solid #ddd",
        borderRadius: "5px",
        margin: "2px",
        display: "inline-block",
        fontSize: "small",
        boxShadow: "0px 1px 1px rgba(0, 0, 0, 0.4)",
        cursor: "pointer",
      }}
      onClick={() => {
        addTagOrBan(word, type, 1);
      }}
    >
      {word}
    </div>
  );
};
export default SugItem;
