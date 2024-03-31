const Tag = ({ tag, onRemove }) => {
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
      }}
    >
      {tag}
      <button
        style={{
          backgroundColor: "red",
          border: "none",
          color: "white",
          paddingLeft: "4px",
          paddingRight: "4px",
          paddingBottom: "2px",
          borderRadius: "50%",
          margin: "2px",
          display: "inline-block",
          boxShadow: "2px 2px 4px rgba(0, 0, 0, 0.4)",
        }}
        onClick={() => onRemove(tag)}
      >
        x
      </button>
    </div>
  );
};
export default Tag;
