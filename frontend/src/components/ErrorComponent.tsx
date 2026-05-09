import Button from './Button.tsx'

export default function ErrorComponent({ message }: { message: string }) {
  return (
    <div className="flex h-full flex-col items-center justify-center gap-6">
      <div className="text-error text-2xl">{message}</div>
      <Button label="Try again" onClick={() => window.location.reload()} />
    </div>
  )
}