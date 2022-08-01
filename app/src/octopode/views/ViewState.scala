package octopode.views

enum ViewState:
  def next(): ViewState = this match {
    case Menu     => Cashback
    case Cashback => Menu
  }
  case Menu     extends ViewState
  case Cashback extends ViewState
