import Quill, { Delta } from "quill";
import { useEffect, useLayoutEffect, useRef } from "react";
import * as S from "./Styles";

const Editor = ({
  readOnly,
  placeholder,
  value = "",
  defaultValue,
  onTextChange,
  onSelectionChange,
  ref,
}) => {
  const containerRef = useRef(null);
  const defaultValueRef = useRef(defaultValue);
  const onTextChangeRef = useRef(onTextChange);
  const onSelectionChangeRef = useRef(onSelectionChange);

  useLayoutEffect(() => {
    onTextChangeRef.current = onTextChange;
    onSelectionChangeRef.current = onSelectionChange;
  });

  useEffect(() => {
    ref.current?.enable(!readOnly);
  }, [ref, readOnly]);

  useEffect(() => {
    const container = containerRef.current;
    const editorContainer = container.appendChild(
      container.ownerDocument.createElement("div")
    );

    const quill = new Quill(editorContainer, {
      theme: "snow",
      placeholder: placeholder,
      bounds: "#quillContainer",
      // https://stackoverflow.com/questions/59875487/quill-editor-popup-is-being-cutoff-in-the-left-when-we-try-to-add-link
    });

    ref.current = quill;

    if (defaultValueRef.current) {
      quill.setContents(defaultValueRef.current);
    }

    quill.on(Quill.events.TEXT_CHANGE, (...args) => {
      onTextChangeRef.current?.(...args);
    });

    quill.on(Quill.events.SELECTION_CHANGE, (...args) => {
      onSelectionChangeRef.current?.(...args);
    });

    return () => {
      ref.current = null;
      container.innerHTML = value;
    };
  }, [ref]);

  return (
    <S.TextEditor>
      <div id="quillContainer" ref={containerRef}></div>
    </S.TextEditor>
  );
};

Editor.displayName = "Editor";

export default Editor;
