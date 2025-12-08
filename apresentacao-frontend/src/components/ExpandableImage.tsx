import type { ImgHTMLAttributes } from "react";
import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from "./ui/dialog";

interface ExpandableImageProps extends ImgHTMLAttributes<HTMLImageElement> {}

const ExpandableImage = (props: ExpandableImageProps) => {
  return (
    <Dialog>
      <DialogTrigger className="cursor-pointer hover:scale-105 transition-all w-full h-full">
        <img {...props} />
      </DialogTrigger>
      <DialogContent className="min-w-[70%]">
        <DialogHeader>
          <DialogTitle>Imagem</DialogTitle>
        </DialogHeader>
        <img
          src={props.src}
          alt="imagem indisponível"
          className="w-full h-full object-cover rounded-lg"
        />
      </DialogContent>
    </Dialog>
  );
};

export default ExpandableImage;
