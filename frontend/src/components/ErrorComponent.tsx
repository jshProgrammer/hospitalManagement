import Button from './Button.tsx'

type ErrorComponentProps = {
  message: string
  onRetry: () => void
}
export default function ErrorComponent({ message, onRetry }: ErrorComponentProps) {
  return (
    <div className="flex h-full flex-col items-center justify-center gap-5 px-6 text-center">
      <div className="text-error text-lg font-semibold">{message}</div>
      <Button label="Try again" onClick={onRetry} />
    </div>
  )
}