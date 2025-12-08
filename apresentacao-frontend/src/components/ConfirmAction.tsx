import {
  AlertDialog,
  AlertDialogCancel,
  AlertDialogContent,
  AlertDialogDescription,
  AlertDialogFooter,
  AlertDialogHeader,
  AlertDialogTitle,
  AlertDialogTrigger,
} from "@/components/ui/alert-dialog";
import {
  type ReactElement,
  type ReactNode,
  useState,
  useTransition,
} from "react";
import { Button, buttonVariants } from "./ui/button";

interface ConfirmActionProps {
  title: string;
  description: string;
  children: ReactNode;
  onConfirm?: () => void | Promise<void>;
  isPositive?: boolean;
  confirmButtonText?: string;
  confirmButtonIcon?: ReactElement;
}

const ConfirmAction = ({
  children,
  description,
  title,
  onConfirm,
  confirmButtonIcon,
  confirmButtonText,
  isPositive,
}: ConfirmActionProps) => {
  const [open, setOpen] = useState(false);

  const [isLoading, startAsync] = useTransition();

  function handleClick(e: React.MouseEvent) {
    e.stopPropagation();

    startAsync(async () => {
      if (onConfirm) {
        await onConfirm();
        setOpen(false);
      }
    });
  }

  return (
    <AlertDialog open={open} onOpenChange={setOpen}>
      <AlertDialogTrigger asChild>{children}</AlertDialogTrigger>
      <AlertDialogContent>
        <AlertDialogHeader>
          <AlertDialogTitle>{title}</AlertDialogTitle>
          <AlertDialogDescription className="items-center">
            {description}
          </AlertDialogDescription>
        </AlertDialogHeader>
        <AlertDialogFooter>
          <AlertDialogCancel
            type="button"
            className={buttonVariants({ variant: "outline" })}
            onClick={(e) => e.stopPropagation()}
          >
            Cancelar
          </AlertDialogCancel>
          <Button
            variant={isPositive ? "default" : "destructive"}
            className="text-white font-medium"
            onClick={handleClick}
            isLoading={isLoading}
          >
            {confirmButtonText ?? "Confirmar"}
            {confirmButtonIcon}
          </Button>
        </AlertDialogFooter>
      </AlertDialogContent>
    </AlertDialog>
  );
};

export default ConfirmAction;
