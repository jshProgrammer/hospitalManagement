import Button from './Button.tsx'

type ErrorComponentProps = {
  message: string
  onRetry: () => void
}
export default function ErrorComponent({ message, onRetry }: ErrorComponentProps) {
  return (
    <div className="flex h-full flex-col items-center justify-center gap-6">
      <div className="text-error text-2xl">{message}</div>
      <Button label="Try again" onClick={onRetry} />
    </div>
  )
}