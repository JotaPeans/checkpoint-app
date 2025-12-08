import { useState } from "react";
import { Popover, PopoverContent, PopoverTrigger } from "./popover";
import { Button } from "./button";
import { Check, ChevronsUpDown } from "lucide-react";
import {
  Command,
  CommandEmpty,
  CommandGroup,
  CommandInput,
  CommandItem,
  CommandList,
} from "./command";
import { cn } from "@/lib/utils";

interface ComboBoxProps {
  data: {
    label: string;
    value: string;
  }[];
  onValueChange: (value: string) => void;
  className?: string;
  popoverClassName?: string;
  disabledItems?: string[];
  id?: string;
}

const ComboBox = ({
  data,
  onValueChange,
  className,
  popoverClassName,
  id,
  disabledItems,
}: ComboBoxProps) => {
  const [open, setOpen] = useState(false);
  const [value, setValue] = useState<string>("");

  return (
    <Popover open={open} onOpenChange={setOpen}>
      <PopoverTrigger asChild>
        <Button
          variant="outline"
          role="combobox"
          aria-expanded={open}
          id={id}
          className={cn("w-[200px] justify-between", className)}
        >
          <span className="truncate">
            {value
              ? data.find((item) => item.value === value)?.label
              : "Selecione um item..."}
          </span>
          <ChevronsUpDown className="opacity-50" />
        </Button>
      </PopoverTrigger>
      <PopoverContent
        className={cn("w-full p-0", popoverClassName)}
        side="bottom"
      >
        <Command>
          <CommandInput placeholder="Buscar item..." className="h-9" />
          <CommandList>
            <CommandEmpty>Nenhum item encontrado</CommandEmpty>
            <CommandGroup>
              {data.map((item) => (
                <CommandItem
                  disabled={disabledItems?.includes(item.value)}
                  key={item.value}
                  value={item.value.toString()}
                  onSelect={(currentValue) => {
                    setValue(currentValue === value ? "" : currentValue);
                    onValueChange(currentValue);
                    setOpen(false);
                  }}
                >
                  {item.label}
                  <Check
                    className={cn(
                      "ml-auto",
                      value === item.value ? "opacity-100" : "opacity-0"
                    )}
                  />
                </CommandItem>
              ))}
            </CommandGroup>
          </CommandList>
        </Command>
      </PopoverContent>
    </Popover>
  );
};

export default ComboBox;
